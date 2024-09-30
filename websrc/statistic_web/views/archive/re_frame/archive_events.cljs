(ns statistic-web.views.archive.re-frame.archive-events
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]
            [statistic-web.re-frame.global-events :as global-events]))

(re-frame/reg-event-fx
  :archive-load
  (fn [{:keys [db]} _event]
    {:http-xhrio {:method          :get
                  :uri             "/data/matches"
                  :params          {:space (get-in db [:space :id])}
                  :format          (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-failure      [::global-events/path-change :error]
                  :on-success      [::global-events/param-change [:matches] :archive]}}))