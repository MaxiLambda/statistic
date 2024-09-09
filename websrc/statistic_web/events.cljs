(ns statistic-web.events
  (:require
    [ajax.core :as ajax]
    [day8.re-frame.http-fx]
    [re-frame.core :as re-frame]                            ;;necessary of effects in reg-event-fx
    [statistic-web.db :as db]))

(defn load-view-event-key [view]
  "gets the :view map from the db and dispatches a ::<:name view>-load event"
  (-> view
      :name
      name
      (str "-load")
      keyword))

;; Event Handling - STEP 2

;;define Handler for Global initialization
(re-frame/reg-event-fx
  ::initialize-db
  (fn [_cofx _event]
    {:db       db/default-db
     :dispatch [(load-view-event-key (:view db/default-db))]}))


;;handles view change
(re-frame/reg-event-fx
  ::path-change
  (fn [{:keys [db]} [_event-key new-view]]
    {:db       (assoc db :view
                         ;;add empty params to new view, if no value for params is set in new-view
                         (merge {:params {}} new-view))
     :dispatch [(load-view-event-key new-view)]
     }))

;;handles view param change
(re-frame/reg-event-db
  ::param-change
  (fn [db [_event-key view-name params-map]]
    (if (= (-> db :view :name) view-name)
      ;;update the params by updating via merging the new params
      (doto (update-in db [:view :params] merge params-map) println)
      ;;params for wrong view changed
      (do
        (println "Params changed for view" view-name "but current view is" (-> db :view :name))
        db))))

(re-frame/reg-event-fx
  :home-load
  (fn [_cofx _event]
    {:http-xhrio {:method          :get
                  :uri             "/wins"
                  :timeout         5000
                  :format          (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-failure      [::path-change :failure]
                  :on-success      [::wins-fetched :home]
                  }}))

(re-frame/reg-event-fx
  ::wins-fetched
  (fn [_cofx [_event-key view body]]
    {:dispatch [::param-change view {:wins body}]}))