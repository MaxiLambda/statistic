(ns statistic-web.re-frame.global-events
  (:require
    [ajax.core :as ajax]
    [day8.re-frame.http-fx]
    [re-frame.core :as re-frame]                            ;;necessary of effects in reg-event-fx
    [statistic-web.re-frame.initial-state :as db]))

;;used to automatically dispatch an event to load all data for a specific view
(defn load-view-event-key [view]
  "gets the :view map from the db and dispatches a ::<:name view>-load event"
  (-> view
      :name
      name
      (str "-load")
      keyword))

;;define Handler for Global initialization
(re-frame/reg-event-fx
  ::initialize-db
  (fn [_cofx _event]
    {:db       db/default-db
     ;;dispatch load view event for initial view
     :dispatch [(load-view-event-key (:view db/default-db))]}))

;;handles view change
;;supports change by "<:view>" and "{:name <:view> :params {...}}"
(defmulti path-change (fn [_ [_ param]] (keyword? param)))
(defmethod path-change true [cofx [event-key new-view]]
  (path-change cofx [event-key {:name new-view}]))
(defmethod path-change false [{:keys [db]} [_event-key new-view]]
  {:db       (assoc db :view
                       ;;add empty params to new view, if no value for params is set in new-view
                       (merge {:params {}} new-view))
   :dispatch [(load-view-event-key new-view)]})

;;handler for view change
(re-frame/reg-event-fx
  ::path-change
  path-change)

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

(re-frame/reg-event-db
  ::param-clear
  (fn [db [_event-key view-name & param-key-list]]
    (let [current-view (-> db :view :name)]
      (if (= current-view view-name)
        ;;update the params by removing the values with the keys from param-key-list
        (update-in db [:view :params] #(apply dissoc % param-key-list))
        ;;params changed but change was requested from/for different view
        ;;therefore don't change db
        (do
          (println "Params changed for view" view-name "but current view is" current-view)
          db)))))

(re-frame/reg-event-fx
  ::dispatch-multi-ordered
  (fn [_cofx [_event-key & events]]
    {:fx events}))


"{:discipline} can be provided to limit the returned tags to the ones existing withing the given discipline"
(re-frame/reg-event-fx
  ::fetch-tags
  (fn [_cofx [_event-key success-event & {discipline :discipline}]]
    (let [req {:method          :get
               :uri             "/data/tags"
               :format          (ajax/json-request-format)
               :response-format (ajax/json-response-format {:keywords? true})
               :on-failure      [::path-change {:name :failure}]
               :on-success      [success-event]}]
      {:http-xhrio (if (nil? discipline)
                     req
                     (assoc req :params {:discipline discipline}))})))

(re-frame/reg-event-fx
  ::fetch-disciplines
  (fn [_cofx [_event-key success-event]]
    {:http-xhrio {:method          :get
                  :uri             "/data/disciplines"
                  :format          (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-failure      [::path-change {:name :failure}]
                  :on-success      [success-event]}}))
