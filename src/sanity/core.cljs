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

(defn setup []
  (reset! app-map (init-map))
  ;; Notice that it doesn't care if the map is google or mapbox.
  ;; The correct implementation will be used regardless.
  (sp/add-marker! @app-map {:position {:lat 14.6091
                                       :lng 121.0223}}))

(defn ^:export switch-provider []
  (swap! use-google not)
  (setup))

(defn ^:export init []
  (let [switch-button (js/document.getElementById "switch-button")]
    (.addEventListener switch-button "click" switch-provider))
  (setup))
