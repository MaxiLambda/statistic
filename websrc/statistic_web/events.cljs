(ns statistic-web.events
  (:require
    [ajax.core :as ajax]
    [day8.re-frame.http-fx]
    [re-frame.core :as re-frame]                            ;;necessary of effects in reg-event-fx
    [statistic-web.db :as db]))

;;todo consider splitting in view specific events

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
     ;;dispatch load view event for initial view
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
    (let [current-view (-> db :view :name)]
      (if (= current-view view-name)
        ;;update the params by updating via merging the new params
        (update-in db [:view :params] merge params-map)
        ;;params changed but change was requested from/for different view
        ;;therefore don't change db
        (do
          (println "Params changed for view" view-name "but current view is" current-view)
          db)))))

(re-frame/reg-event-fx
  :home-load
  (fn [_cofx _event]
    {:http-xhrio {:method          :get
                  :uri             "/wins"
                  :format          (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-failure      [::path-change {:name :failure}]
                  :on-success      [::wins-fetched :home]
                  }}))

(re-frame/reg-event-fx
  ::wins-fetched
  (fn [_cofx [_event-key view body]]
    ;;dispatch event to add the wins under the :wins key to the :params map
    {:dispatch [::param-change view {:wins body}]}))