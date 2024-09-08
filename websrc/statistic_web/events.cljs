(ns statistic-web.events
  (:require
    [re-frame.core :as re-frame]
    [statistic-web.db :as db]))

;; Event Handling - STEP 2

;;define Handler for Global initialization
(re-frame/reg-event-db
  ::initialize-db
  (fn [_ _]
    db/default-db))

;;handles path or parameter
(re-frame/reg-event-db
  ::path-param-change
  (fn [db event])
  )