(ns statistic-web.events.management-events
  (:require [re-frame.core :as re-frame]
            [day8.re-frame.http-fx]))

(re-frame/reg-event-fx
  :management-load
  (fn [_cofx _event]
    {}))