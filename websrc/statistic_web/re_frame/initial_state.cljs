(ns statistic-web.re-frame.initial-state)

;;The initial global state
(def default-db
  {:view {:name   :login
          :params {}}
   ;;indicates the current space -> set by all logins but the admin login
   :space nil
   ;;the current mode out of "edit" "view" "admin" "none"
   ;;"admin" indicates admin mode, therefore :space is nil
   ;;"none" is the initial value
   :mode "none"})
