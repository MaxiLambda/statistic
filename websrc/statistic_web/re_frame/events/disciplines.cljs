(ns statistic-web.re-frame.events.disciplines
  (:require [ajax.core :as ajax]
            [statistic-web.re-frame.global-events :as global-events]
            [re-frame.core :as re-frame]))

(re-frame/reg-event-fx
  ::fetch-disciplines
  (fn [{:keys [db]} [_event-key success-event]]
    {:http-xhrio {:method          :get
                  :uri             "/view/data/disciplines"
                  :params         {:space (get-in db [:space :id])}
                  :format          (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-failure      [::global-events/path-change :error]
                  :on-success      success-event}}))
