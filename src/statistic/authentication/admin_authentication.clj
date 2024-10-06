(ns statistic.authentication.admin-authentication
  (:require [statistic.db.tables.spaces :as spaces]))

;;TODO maybe write to config file instead
(defn admin-credentials []
  {:user     (or (System/getenv "ADMIN_NAME") "admin")
   :password (or (System/getenv "ADMIN_PASSWORD") "admin")})

(defn edit-authenticated?
  "check if the provided user authentication for the given space matches edit authentication"
  [user password {body :body params :params :as req}]
  (println req)
  (let [space-id (or (:space body) (-> params :space Integer/parseInt))
        space (spaces/get-by-id {:id space-id})]
    (and (= user (-> space :name (str "-admin")))
         (= password (:edit_password space)))))

(defn view-authenticated?
  "check if the provided user authentication for the given space matches view authentication"
  [user password {body :body params :params :as req}]
  (let [space-id (or (:space body) (-> params :space Integer/parseInt))
        space (spaces/get-by-id {:id space-id})]
    (or (and (= user (:name space))
             (= password (:view_password space)))
        (edit-authenticated? user password req))))

(defn admin-authenticated? [user password _req]
  "check if the provided user authentication matches the global admin authentication"
  (and (= (:user (admin-credentials)) user)
       (= (:password (admin-credentials)) password)))