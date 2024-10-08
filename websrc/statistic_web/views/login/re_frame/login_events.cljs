(ns statistic-web.views.login.re-frame.login-events
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]
            [statistic-web.re-frame.global-events :as global-events]
            [statistic-web.re-frame.events.spaces :as spaces-events]
            [statistic-web.views.login.login-data :as data]
            [statistic-web.views.login.re-frame.login-request-body :refer [transform-body]]))

(re-frame/reg-event-fx
  :login-load
  (fn [_cofx _event]
    (reset! data/login-data data/initial-data)
    {:dispatch [::spaces-events/fetch-spaces [::global-events/param-change [:spaces] :login]]}))

(re-frame/reg-event-fx
  ::login
  ;;{:type oneOf {"admin", "space"
  ;; :pw oneOf {string, nil}}
  ;; :name        -> string
  ;;when :type "space" there are additional keys available:
  ;;  :id         -> int
  ;;  :permission -> oneOf {"edit", "view"}
  (fn [_cofx [_event-key {:keys [id name] :as login-data}]]
    {:http-xhrio {:method          :post
                  :uri             "/login"
                  :params          (transform-body login-data)
                  :format          (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-failure      [::global-events/path-change :error]
                  :on-success      [::authenticated name id]}}))

(re-frame/reg-event-fx
  ::authenticated
  (fn [{:keys [db]} [_event-key name id {mode :mode}]]
    (let [data (if (nil? id)
                 {:mode mode}
                 {:mode  mode
                  :space {:id   id
                          :name name}})]
      {:db (merge db data)
       ;;TODO dispatch admins to :space-management and others to :leaderboard
       :dispatch [::global-events/path-change :login]})))

