(ns statistic-web.views.login.login-data
  (:require [reagent.core :as r]))

(def initial-data {:type ""})

;;{:type oneOf {"admin", "space"
;; :pw oneOf {string, nil}}
;; :name        -> string
;;when :type "space" there are additional keys available:
;;  :id         -> int
;;  :permission -> oneOf {"edit", "view"}
(defonce login-data (r/atom initial-data))

(defn to-login [spaces selected-id]
  (let [space (first (filter #(= selected-id (:id %)) spaces))]
    (if (nil? space)
      {:type "admin"}
      (assoc space :type "space"))))