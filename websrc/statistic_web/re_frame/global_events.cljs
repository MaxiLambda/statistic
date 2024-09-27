(ns statistic-web.re-frame.global-events
  (:require
    [day8.re-frame.http-fx]                                 ;;necessary for effects in reg-event-fx
    [re-frame.core :as re-frame]
    [statistic-web.re-frame.initial-state :as db]))

;;used to automatically dispatch an event to load all data for a specific view
(defn load-view-event-key [view]
  "gets the :view :name value from the db and dispatches a ::<:name view>-load event"
  (-> view
      name
      (str "-load")
      keyword))

;;define Handler for Global initialization
(re-frame/reg-event-fx
  ::initialize-db
  (fn [_cofx _event]
    {:db       db/default-db
     ;;dispatch load view event for initial view
     :dispatch [(load-view-event-key (-> db/default-db :view :name))]}))

;;handler for view change
(re-frame/reg-event-fx
  ::path-change
  (fn [{:keys [db]} [_event-key key & load-params]]
       {:db       (assoc-in db [:view :name] key)
        :dispatch [(load-view-event-key key) load-params]}))

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