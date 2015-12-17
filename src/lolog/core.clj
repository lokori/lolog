(ns lolog.core
  (:require [clojure.tools.logging :as log]))

(def ^:private requestid (atom 1))

(defn wrap-debug-request
  "Ring wrapper which logs everything about a request"
  [ring-handler]
  (fn [request]
    (log/info request)
    (ring-handler request)))

(defn ^:private http-method->str [keyword-or-str]
  (clojure.string/upper-case (name keyword-or-str)))

(defn ^:private print-keys
  "Takes pairs from the vector, the first element of a pair is a string key and the second element is a function which maps something from the request. Concatenates the result into a string."
  [requ fm]
  (subs (apply str (map (fn [[k v]] (str " ," k ": " (v requ))) fm)) 2))

(defn start-str 
  ([id req custom-mapping]
    (let [default-mapping [[" remote-addr" :remote-addr]
                           ["method" #(http-method->str (:request-method %))]
                           ["uri" :uri]
                           ["query-string" :query-string]
                           ["user-agent" #(get-in % [:headers "user-agent"])]
                           ["referer" #(get-in % [:headers "referer"])]]]
      (str "Request " id " start. "
        (print-keys req (concat default-mapping custom-mapping)))))
 
  ([id req]
    (start-str id req nil)))
;       " remote-addr: " (:remote-addr req) 
;       " ,method: " (http-method->str (:request-method req))
;       " ,uri: " (:uri req) 
;       " ,query-string: " (:query-string req)
;       " ,user-agent: " (get (:headers req) "user-agent")
;       " ,referer: " (get (:headers req) "referer")))

(defn end-str [id req total]
  (str "Request " id " end. Duration: " total " ms. uri: " (:uri req)))

(defn wrap-log-request [ring-handler & custom-mapping]
  "Logging middleware. Basic information + duration and unique id for performance analysis"
  (fn [req]
    (let [id (swap! requestid inc)
          start (System/currentTimeMillis)]
      (log/info (start-str id req custom-mapping))
      (let [response (ring-handler req)
            finish (System/currentTimeMillis)
            total  (- finish start)]
        (log/info (end-str id req total))
        response
        ))))