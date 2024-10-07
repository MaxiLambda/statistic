(ns statistic.rest.controller.login-controller
  (:require [clojure.data.json :as json]
            [compojure.core :refer [POST defroutes]]
            [ring.middleware.cookies :as cookies]
            [ring.util.response :as response]
            [statistic.authentication.auth-cookie :refer [cookie-name separator]]
            [statistic.authentication.authentication-check :as check]
            [statistic.authentication.hash :as hash]))


(defn admin-authentication [username pw]
  (let [password (hash/hash-pw pw)]
    (if (check/admin-authenticated? username password)
      (cookies/cookies-response
        (response/set-cookie {:status  200
                              :headers {"Content-Type" "text/json"}
                              :body    (json/write-str {:mode "admin"})}
                             cookie-name (str username separator password)))
      {:status 401})))

(defn user-authentication [username pw space]
  (let [password (hash/hash-pw pw)
        edit? (check/edit-authenticated? username password space)
        view? (check/view-authenticated? username password space)
        mode (or (when edit? "edit") (when view? "view"))]
    (if (nil? mode)
      {:status 401}
      (cookies/cookies-response
        (response/set-cookie {:status  200
                              :headers {"Content-Type" "text/json"}
                              :body    (json/write-str {:mode mode})}
                             cookie-name (str username separator password separator space))))))

(defn login-handler
  "the request body is expected to look like this:
  {:username
   :password}

   if an edit/view login is tried there is an additional :space key to
   denote the space in which the authentication should be valid.
   An admin login is missing the :space key."
  [{body :body}]
  (println body)
  (let [{:keys [username password space]} body]
    (if (nil? space)
      (admin-authentication username password)
      (user-authentication username password space))))

(defroutes routes
           (POST "/login" [] login-handler))