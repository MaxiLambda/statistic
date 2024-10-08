(ns statistic.authentication.authentication-check
  (:require [statistic.authentication.hash :as hash]
            [statistic.db.tables.spaces :as spaces]))

(defn to-integer [num]
  (if (= Integer (class num))
    num
    (Integer/parseInt num)))

;;TODO maybe write to config file instead
(defn admin-credentials []
  {:user     (or (System/getenv "ADMIN_NAME") "admin")
   :password (hash/hash-pw (or (System/getenv "ADMIN_PASSWORD") "admin"))})

(defn edit-authenticated?
  "check if the provided user authentication for the given space matches edit authentication.
  Space id is used with this syntax to prevent arity expectations when it is missing."
  [user password & [space-id]]
  (if (nil? space-id)
    false
    (let [space (spaces/get-by-id {:id (to-integer space-id)})]
      (and (= user (-> space :name (str "-admin")))
           (= password (:edit_password space))))))

(defn view-authenticated?
  "check if the provided user authentication for the given space matches view authentication.
  Space id is used with this syntax to prevent arity expectations when it is missing."
  [user password & [space-id]]
  (if (nil? space-id)
    false
    (let [space (spaces/get-by-id {:id (to-integer space-id)})]
      (or (and (= user (:name space))
               (= password (:view_password space)))
          (edit-authenticated? user password space-id)))))

(defn admin-authenticated? [user password]
  "check if the provided user authentication matches the global admin authentication"
  (and (= (:user (admin-credentials)) user)
       (= (:password (admin-credentials)) password)))