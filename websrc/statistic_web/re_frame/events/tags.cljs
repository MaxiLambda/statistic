(ns statistic-web.re-frame.events.tags
  (:require [ajax.core :as ajax]
            [statistic-web.re-frame.global-events :as global-events]
            [re-frame.core :as re-frame]))

"{:discipline} can be provided to limit the returned tags to the ones existing withing the given discipline"
(re-frame/reg-event-fx
  ::fetch-tags
  (fn [_cofx [_event-key success-event & {discipline :discipline}]]
    (let [req {:method          :get
               :uri             "/data/tags"
               :format          (ajax/json-request-format)
               :response-format (ajax/json-response-format {:keywords? true})
               :on-failure      [::global-events/path-change :error]
               :on-success      success-event}]
      {:http-xhrio (if (nil? discipline)
                     req
                     (assoc req :params {:discipline discipline}))})))

