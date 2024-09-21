(ns statistic-web.views.archive.re-frame.archive-subs
  (:require [re-frame.core :as re-frame]))

;;subscription handler for matches, only setup by :archives at the moment
(re-frame/reg-sub
  ::matches
  (fn [db]
    (-> db :view :params :matches)))

