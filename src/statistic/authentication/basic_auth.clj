(ns statistic.authentication.basic-auth
  (:require [clojure.string :as str]))

(defn wrap-add-basic-prefix [handler]
  (fn [request]
    (let [auth-header (get-in request [:headers "authorization"])]
      (if (and auth-header (not (str/includes? auth-header "Basic ")))
        ;; Add "Basic " prefix if missing
        (let [updated-request (assoc-in request [:headers "authorization"]
                                        (str "Basic " auth-header))]
          (handler updated-request))
        ;; If the prefix is already present, pass the request unchanged
        (handler request)))))