(ns statistic-web.views.leaderboard.re-frame.leaderboard-events
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]
            [statistic-web.re-frame.events.disciplines :as discipline-events]
            [statistic-web.re-frame.global-events :as global-events]))

(re-frame/reg-event-fx
  :leaderboard-load
  (fn [_cofx _event]
    {:dispatch-n [[::fetch-wins]
                  [::discipline-events/fetch-disciplines [::global-events/param-change [:disciplines] :leaderboard]]]}))

(re-frame/reg-event-fx
  ::fetch-wins
  ;;modifiers are an optional map of {:discipline :tag}, :tag is only handled if :discipline is given
  (fn [_cofx [_event-key & {:as modifiers}]]
    {:http-xhrio {:method          :get
                  :uri             "/wins"
                  :params          (select-keys modifiers [:tag :discipline])
                  :format          (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-failure      [::global-events/path-change :error]
                  :on-success      [::global-events/param-change [:wins] :leaderboard]}}))