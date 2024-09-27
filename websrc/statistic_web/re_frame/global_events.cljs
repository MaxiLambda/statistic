(ns statistic-web.re-frame.global-events
  (:require
    [day8.re-frame.http-fx]                                 ;;necessary for effects in reg-event-fx
    [re-frame.core :as re-frame]
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

;;handles the change of parameters
;;body is the new value, param-path describes the path or key of the parameter
(re-frame/reg-event-db
  ::param-change
  (fn [db [_event-key  param-path view-name body]]
    (let [current-view (-> db :view :name)]
      (if (= current-view view-name)
        ;;update the params by updating via merging the new params
        (assoc-in db (concat [:view :params] param-path) body)
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