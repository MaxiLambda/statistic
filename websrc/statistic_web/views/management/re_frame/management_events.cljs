(ns statistic-web.views.management.re-frame.management-events
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]
            [statistic-web.re-frame.global-events :as global-events]
            [statistic-web.re-frame.events.disciplines :as discipline-events]
            [statistic-web.re-frame.events.tags :as tag-events]
            [statistic-web.views.management.management-data :as data]))

(re-frame/reg-event-fx
  :management-load
  (fn [_cofx _event]
    (reset! data/match-form (data/initial-match-form))
    (reset! data/new-player (data/initial-new-player))
    {:http-xhrio {:method          :get
                  :uri             "/players"
                  :format          (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-failure      [::global-events/path-change {:name :failure}]
                  :on-success      [::global-events/param-change [:players] :management]}
     :dispatch-n [[::tag-events/fetch-tags [::global-events/param-change [:tags] :management]]
                  [::discipline-events/fetch-disciplines [::global-events/param-change [:disciplines] :management]]]}))


(re-frame/reg-event-fx
  ::create-player
  (fn [_cofx [_event-key params]]
    {:http-xhrio {:method          :post
                  :uri             "/admin/players/create"
                  :params          params
                  :format          (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-failure      [::global-events/path-change {:name :failure}]
                  :on-success      [:management-load]}}))

(re-frame/reg-event-fx
  ::create-match
  (fn [_cofx [_event-key params]]
    {:http-xhrio {:method          :post
                  :uri             "/admin/matches/create"
                  :params          params
                  :format          (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-failure      [::global-events/path-change {:name :failure}]
                  :on-success      [:management-load]}}))