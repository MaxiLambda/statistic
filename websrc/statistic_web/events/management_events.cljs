(ns statistic-web.events.management-events
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]
            [statistic-web.events.global-events :as global-events]))

(re-frame/reg-event-fx
  :management-load
  (fn [_cofx _event]
    {:http-xhrio {:method          :get
                  :uri             "/players"
                  :format          (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-failure      [::global-events/path-change {:name :failure}]
                  :on-success      [::players-fetched :management]}}))

(re-frame/reg-event-fx
  ::players-fetched
  (fn [_cofx [_event-key view body]]
    ;;dispatch event to add the players under the :players key to the :params map
    {:dispatch [::global-events/param-change view {:players body}]}))