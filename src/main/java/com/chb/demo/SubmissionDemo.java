package com.chb.demo;

import com.indico.storage.*;
import com.indico.type.*;
import com.indico.mutation.*;
import com.indico.query.*;
import com.indico.*;
import java.util.*;
import java.io.*;
import org.json.*;

/**
 * An example of executing a submission to a workflow
 */
public class SubmissionDemo{

    final static int WORKFLOW_ID = 8;

     /**
     * create config & client instance
     */
    private static IndicoClient getClient() throws Exception{

        InputStream tokenIS = SubmissionDemo.class.getClassLoader().getResourceAsStream("indico_api_token.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(tokenIS));
        String token = reader.readLine();

        System.out.println("token: " + token);

        IndicoConfig config = new IndicoConfig
            .Builder()
            .host("app.indico.io")
            .apiToken(token)
            .build();
        return new IndicoKtorClient(config);
    }

    public static void main(String[] args) throws Exception{


        IndicoClient client = getClient();

        /**
         * get submission by client
         */
        WorkflowSubmission workflowSubmission = client.workflowSubmission();

        /**
         * fill in new pdf path into specific workflow and execute
         */

        String path = SubmissionDemo.class.getClassLoader().getResource("docForSubmissionDemo.pdf").getPath();
        int start = path.indexOf("/") ;
        path = path.substring(start);
        ArrayList<String> files = new ArrayList<>();
        files.add(path);
        List<Integer> submissionIds = workflowSubmission.files(files).workflowId(WORKFLOW_ID).execute();
        int submissionId = submissionIds.get(0);


        /**
         * actual execute the submission
         */
        Job job = client.submissionResult().submission(submissionId).execute();

        /**
         * wait the execution finish
         */
        while (job.status() == JobStatus.PENDING) {
            Thread.sleep(1000);
            System.out.println("Job Status: " + job.status());
        }

        JSONObject obj = job.result();
        String url = obj.getString("url");
        RetrieveBlob retrieveBlob = client.retrieveBlob();
        Blob blob = retrieveBlob.url(url).execute();

        /**
         * clean resoures which opend befor
         */
        blob.close();
        System.out.println(blob.asString());
        client.updateSubmission().submissionId(submissionId).retrieved(true).execute();

    }


}
