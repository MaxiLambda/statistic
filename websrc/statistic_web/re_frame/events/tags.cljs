(ns statistic-web.re-frame.events.tags
  (:require [ajax.core :as ajax]
            [statistic-web.re-frame.global-events :as global-events]
            [re-frame.core :as re-frame]))


;;TODO it should now be possible to remove the hardcoded {:space 1} in the backend!

"{:discipline} can be provided to limit the returned tags to the ones existing withing the given discipline"
(re-frame/reg-event-fx
  ::fetch-tags
  (fn [{:keys [db]} [_event-key success-event & {discipline :discipline}]]
    (let [req {:method          :get
               :uri             "/view/data/tags"
               :params          {:space (get-in db [:space :id])}
               :format          (ajax/json-request-format)
               :response-format (ajax/json-response-format {:keywords? true})
               :on-failure      [::global-events/path-change :error]
               :on-success      success-event}]
      {:http-xhrio (if (nil? discipline)
                     req
                     (update-in req [:params] merge {:discipline discipline}))})))

