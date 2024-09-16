(ns statistic-web.events.management-events
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]
            [statistic-web.events.global-events :as global-events]))

(re-frame/reg-event-fx
  :management-load
  (fn [_cofx _event]
    {:http-xhrio [{:method          :get
                   :uri             "/players"
                   :format          (ajax/json-request-format)
                   :response-format (ajax/json-response-format {:keywords? true})
                   :on-failure      [::global-events/path-change {:name :failure}]
                   :on-success      [::players-fetched]}
                  {:method          :get
                   :uri             "/data/tags"
                   :format          (ajax/json-request-format)
                   :response-format (ajax/json-response-format {:keywords? true})
                   :on-failure      [::global-events/path-change {:name :failure}]
                   :on-success      [::tags-fetched]}
                  {:method          :get
                   :uri             "/data/disciplines"
                   :format          (ajax/json-request-format)
                   :response-format (ajax/json-response-format {:keywords? true})
                   :on-failure      [::global-events/path-change {:name :failure}]
                   :on-success      [::disciplines-fetched]}]}))

(re-frame/reg-event-fx
  ::players-fetched
  (fn [_cofx [_event-key body]]
    ;;dispatch event to add the players under the :players key to the :params map
    {:dispatch [::global-events/param-change :management {:players body}]}))

(re-frame/reg-event-fx
  ::disciplines-fetched
  (fn [_cofx [_event-key body]]
    ;;dispatch event to add the disciplines under the :disciplines key to the :params map
    {:dispatch [::global-events/param-change :management {:disciplines body}]}))

(re-frame/reg-event-fx
  ::tags-fetched
  (fn [_cofx [_event-key body]]
    ;;dispatch event to add the tags under the :tags key to the :params map
    {:dispatch [::global-events/param-change :management {:tags body}]}))