(ns statistic-web.views.leaderboard.re-frame.leaderboard-events
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]
            [statistic-web.re-frame.global-events :as global-events]))

;;modifiers are an optional map of {:discipline :tag}, :tag is only handled if :discipline is given
(re-frame/reg-event-fx
  :leaderboard-load
  (fn [_cofx _event]
    ;;TODO UI of leaderboard
    ;;TODO remove prints
    {:http-xhrio (global-events/fetch-disciplines ::disciplines-fetched)
     :dispatch   [::fetch-wins]}))

;;[::fetch-tags (select-keys modifiers [:discipline])]

(re-frame/reg-event-fx
  ::fetch-wins
  (fn [_cofx [_event-key & {:as modifiers}]]
    {:http-xhrio {:method          :get
                  :uri             "/wins"
                  :params          (select-keys modifiers [:tag :discipline])
                  :format          (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-failure      [::global-events/path-change {:name :failure}]
                  :on-success      [::wins-fetched :leaderboard]}}))

;;this is a separate event so
(re-frame/reg-event-fx
  ::fetch-tags
  (fn [_cofx [_event-key modifier]]
    {:http-xhrio (global-events/fetch-tags ::tags-fetched
                                           (select-keys modifier [:discipline]))}))

(re-frame/reg-event-fx
  ::clear-tags
  (fn [_cofx _event]
    {:dispatch [::global-events/param-clear :leaderboard :tags]}))

(re-frame/reg-event-fx
  ::disciplines-fetched
  (fn [_cofx [_event-key body]]
    ;;dispatch event to add the disciplines under the :disciplines key to the :params map
    {:dispatch [::global-events/param-change :leaderboard {:disciplines body}]}))

(re-frame/reg-event-fx
  ::tags-fetched
  (fn [_cofx [_event-key body]]
    ;;dispatch event to add the tags under the :tags key to the :params map
    {:dispatch [::global-events/param-change :leaderboard {:tags body}]}))


(re-frame/reg-event-fx
  ::wins-fetched
  (fn [_cofx [_event-key view body]]
    ;;dispatch event to add the wins under the :wins key to the :params map
    {:dispatch [::global-events/param-change view {:wins body}]}))