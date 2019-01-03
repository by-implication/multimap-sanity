(ns sanity.core
  (:require [sanity.protocols :as sp]
            [sanity.google]
            [sanity.mapbox]))

(defn ^:export init []
  (let [map-config {:dom-node (js/document.getElementById "map")
                    :zoom     12
                    :center   {:lat 14.6091
                               :lng 121.0223}
                    :style    "https://tiles.stadiamaps.com/styles/alidade_smooth.json"}]
    #_(sanity.google/new-google-map map-config)
    (sanity.mapbox/new-mapbox-map map-config)))
