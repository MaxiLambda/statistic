(ns statistic-web.views.leaderboard.re-frame.leaderboard-events
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]
            [statistic-web.re-frame.global-events :as global-events]))

(re-frame/reg-event-fx
  :leaderboard-load
  (fn [_cofx _event]
    {:http-xhrio {:method          :get
                  :uri             "/wins"
                  :format          (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-failure      [::global-events/path-change {:name :failure}]
                  :on-success      [::wins-fetched :leaderboard]
                  }}))

(re-frame/reg-event-fx
  ::wins-fetched
  (fn [_cofx [_event-key view body]]
    ;;dispatch event to add the wins under the :wins key to the :params map
    {:dispatch [::global-events/param-change view {:wins body}]}))