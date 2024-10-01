(ns statistic-web.views.management.re-frame.management-events
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]
            [statistic-web.re-frame.global-events :as global-events]
            [statistic-web.re-frame.events.disciplines :as discipline-events]
            [statistic-web.re-frame.events.tags :as tag-events]
            [statistic-web.views.management.management-data :as data]))

(re-frame/reg-event-fx
  :management-load
  (fn [{:keys [db]} _event]
    (reset! data/match-form (data/initial-match-form))
    (reset! data/new-player (data/initial-new-player))
    {:http-xhrio {:method          :get
                  :uri             "/view/players"
                  :params          {:space (get-in db [:space :id])}
                  :format          (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-failure      [::global-events/path-change :error]
                  :on-success      [::global-events/param-change [:players] :management]}
     :dispatch-n [[::tag-events/fetch-tags [::global-events/param-change [:tags] :management]]
                  [::discipline-events/fetch-disciplines [::global-events/param-change [:disciplines] :management]]]}))


(re-frame/reg-event-fx
  ::create-player
  (fn [{:keys [db]} [_event-key params]]
    {:http-xhrio {:method          :post
                  :uri             "/edit/players/create"
                  :params          (merge {:space (get-in db [:space :id])} params)
                  :format          (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-failure      [::global-events/path-change :error]
                  :on-success      [:management-load]}}))

(re-frame/reg-event-fx
  ::create-match
  (fn [{:keys [db]} [_event-key params]]
    {:http-xhrio {:method          :post
                  :uri             "/edit/matches/create"
                  :params          (merge {:space (get-in db [:space :id])} params)
                  :format          (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-failure      [::global-events/path-change :error]
                  :on-success      [:management-load]}}))