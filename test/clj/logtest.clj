(ns logtest
  (:require [clojure.test :refer [deftest testing is are]]
            [lolog.core :refer :all]))

(deftest logformat
  (testing "log message format"
    (let [requ  {:uri "lai" :headers {"user-agent" "l" "referer" "r"}   
                 :request-method "l"}]
      (is (= "Request 1 start.  remote-addr:  ,method: L ,uri: lai ,query-string:  ,user-agent: l ,referer: r" (start-str 1 requ)))
      (is (= "Request 1 end. Duration: 200 ms. uri: lai" (end-str 1 requ 200))))))

(deftest logformat-custom
  (testing "log message format with custom log elements"
    (let [requ  {:uri "lai" :headers {"user-agent" "l" "referer" "r"
                                      "oid" "OID"}   
                 :request-method "l"}
          customized [["oid" #(get-in % [:headers "oid"])]]]
      (is (= "Request 1 start.  remote-addr:  ,method: L ,uri: lai ,query-string:  ,user-agent: l ,referer: r ,oid: OID" (start-str 1 requ customized)))
      (is (= "Request 1 end. Duration: 200 ms. uri: lai" (end-str 1 requ 200))))))

(deftest wrapper-fn
  (let [ff (wrap-log-request #(into {} %))
        requ  {:uri "lai" :headers {"user-agent" "l" "referer" "r"}   
                 :request-method "l"}]
    (= requ (ff requ))))
