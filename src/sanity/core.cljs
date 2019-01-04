(ns sanity.core
  (:require [sanity.protocols :as sp]
            [sanity.google]
            [sanity.mapbox]))

(def use-google (atom false))
(def app-map (atom nil))

(defn init-map []
  (let [map-config {:dom-node (js/document.getElementById "map")
                    :zoom     12
                    :center   {:lat 14.6091
                               :lng 121.0223}}]
    (if @use-google
      (sanity.google/new-google-map map-config)
      (sanity.mapbox/new-mapbox-map map-config))))

(defn do-stuff []
  (sp/add-marker! @app-map {:position {:lat 14.6091
                                       :lng 121.0223}}))

(defn ^:export switch-provider []
  (swap! use-google not)
  (reset! app-map (init-map))
  (do-stuff))

(defn ^:export init []
  (let [switch-button (js/document.getElementById "switch-button")]
    (.addEventListener switch-button "click" switch-provider))
  (reset! app-map (init-map))
  (do-stuff))
