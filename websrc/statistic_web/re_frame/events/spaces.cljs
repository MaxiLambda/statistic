(ns statistic-web.re-frame.events.spaces
  (:require [ajax.core :as ajax]
            [statistic-web.re-frame.global-events :as global-events]
            [re-frame.core :as re-frame]))

(re-frame/reg-event-fx
  ::fetch-spaces
  (fn [_cofx [_event-key success-event]]
    {:http-xhrio {:method          :get
                  :uri             "/spaces"
                  :format          (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-failure      [::global-events/path-change :error]
                  :on-success      success-event}}))