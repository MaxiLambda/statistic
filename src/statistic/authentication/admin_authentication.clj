(ns statistic.authentication.admin-authentication)

(defn admin-credentials []
  {:user (or (System/getenv "ADMIN_NAME") "admin")
   :password (or (System/getenv "ADMIN_PASSWORD") "admin")})

(defn authenticated? [user password]
  (and (= (:user (admin-credentials)) user)
       (= (:password (admin-credentials)) password)))