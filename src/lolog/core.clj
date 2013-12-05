(ns lolog.core
  (:require [clojure.tools.logging :as log]))

(def ^:private requestid (atom 1))

(defn wrap-debug-request
  "Ring wrapper which logs everything about a request"
  [ring-handler]
  (fn [request]
    (log/info request)
    (ring-handler request)))

(defn http-method->str [keyword-or-str]
  (clojure.string/upper-case (name keyword-or-str)))

(defn wrap-log-request [ring-handler]
  "Logging middleware. Basic information + duration and unique id for performance analysis"
  (fn [req]
    (let [id (swap! requestid inc)
          start (System/currentTimeMillis)]
      (log/info (str "Request " id " start. "
                     " remote-addr: " (:remote-addr req) 
                     " ,method: " (http-method->str (:request-method req))
                     " ,uri: " (:uri req) 
                     " ,query-string: " (:query-string req)
                     " ,user-agent: " (get (:headers req) "user-agent")
                     " ,referer: " (get (:headers req) "referer")))
      (let [response (ring-handler req)
            finish (System/currentTimeMillis)
            total  (- finish start)]
        (log/info (str "Request " id " end. Duration: " total " ms. uri: " (:uri req)))
        response
        ))))
