(ns statistic-web.views.archive.re-frame.archive-events
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]
            [statistic-web.re-frame.global-events :as global-events]))

(re-frame/reg-event-fx
  :archive-load
  (fn [_cofx _event]
    {:http-xhrio {:method          :get
                  :uri             "/data/matches"
                  :format          (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-failure      [::global-events/path-change {:name :failure}]
                  :on-success      [::matches-fetched]}}))

(re-frame/reg-event-fx
  ::matches-fetched
  (fn [_cofx [_event-key body]]
    {:dispatch [::global-events/param-change :archive {:matches body}]}))