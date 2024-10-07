(ns statistic.authentication.wrap-auth
  (:require [clojure.string :as str]
            [statistic.authentication.auth-cookie :refer [cookie-name separator]])
  (:import (java.util.regex Pattern)))

(defn wrap-auth
  "Secures routes using authenticate to check access.
  Access is granted by a custom cookie containing the username, the password (hash)
  and optionally a space. All these values are separated by a separator-value.

  The values used for the authentication are added to the request under :auth."
  [routes authenticate]
  (fn [{:keys [cookies] :as req}]
    (let [delimiter (-> separator Pattern/quote Pattern/compile)
          params (some-> cookies
                                (get cookie-name)
                                :value
                                (str/split delimiter))]
      (if (apply authenticate params)
        (routes (assoc req :auth params))
        {:status 401
         :body   "access denied"
         }))))