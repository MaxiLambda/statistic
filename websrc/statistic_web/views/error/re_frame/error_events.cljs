(ns statistic-web.views.error.re-frame.error-events
  (:require [re-frame.core :as re-frame]
            [day8.re-frame.http-fx]))

(re-frame/reg-event-db
  :error-load
  (fn [db [_event-key reason]]
    (assoc-in db [:view :params :error] reason)))
