(ns statistic-web.views.react-hooks.use-is-small-view
  (:require ["@mui/material/styles" :refer [useTheme]]
            ["@mui/material/useMediaQuery" :default useMediaQuery]
            ))

;;using the react naming schema, to stress that this function can only be called inside react components
(defn useIsSmallView
  "cljs wrapper around react hooks.
  Returns weather or not the Sceen size is below the 'sm' breakpoint.

  MUST be used inside react components.
  https://github.com/reagent-project/reagent/blob/master/doc/ReactFeatures.md#hooks"
  []
  (let [theme (useTheme)
        is-small (useMediaQuery (.down (.-breakpoints theme) "md"))]
    is-small))


(defn size-dependent-inner [big-children small-children]
  (let [is-small (useIsSmallView)]
    (if is-small small-children big-children)))

(defn size-dependent [big-children small-children]
  [:f> size-dependent-inner big-children small-children])