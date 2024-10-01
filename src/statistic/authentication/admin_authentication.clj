(ns statistic.authentication.admin-authentication
  (:require [statistic.db.tables.spaces :as spaces]))

;;TODO maybe write to config file instead
(defn admin-credentials []
  {:user     (or (System/getenv "ADMIN_NAME") "admin")
   :password (or (System/getenv "ADMIN_PASSWORD") "admin")})

(defn view-authenticated?
  "check if the provided user authentication for the given space matches view authentication"
  [user password {params :params}]
  (let [space (spaces/get-by-id {:id (-> params :space Integer/parseInt)})]
    (and (= user (:name space))
         (= password (:view_password space)))))

(defn edit-authenticated? [user password {params :params}]
  "check if the provided user authentication for the given space matches edit authentication"
  [user password {params :params}]
  (let [space (spaces/get-by-id {:id (-> params :space Integer/parseInt)})]
    (and (= user (-> space :name (str "-admin")))
         (= password (:edit_password space)))))

(defn admin-authenticated? [user password _req]
  "check if the provided user authentication matches the global admin authentication"
  (and (= (:user (admin-credentials)) user)
       (= (:password (admin-credentials)) password)))