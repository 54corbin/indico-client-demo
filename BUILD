
java_binary(
  name = 'submissionDemo',
  srcs = glob(
  [
    "src/main/java/com/chb/demo/*.java",

  ]),
  resources = [

           "src/main/resources/indico_api_token.txt",
  ],
  main_class = "com.chb.demo.SubmissionDemo",
  deps = [
    "@maven//:com_indico_indico_client_java",
    "@maven//:org_json_json",
    "@maven//:org_apache_logging_log4j_log4j_core",
  ],
)

