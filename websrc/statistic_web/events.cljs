(ns statistic-web.events
  (:require
    [re-frame.core :as re-frame]
    [statistic-web.db :as db]))

;; Event Handling - STEP 2

;;define Handler for Global initialization
(re-frame/reg-event-db
  ::initialize-db
  (fn [_db _event]
    db/default-db))

;;handles view change
(re-frame/reg-event-db
  ::path-param-change
  (fn [db [_event-key new-view]]
    (assoc db :view new-view)))