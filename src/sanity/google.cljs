(ns sanity.google
  (:require [sanity.protocols :as sp]))

(extend-type js/google.maps.Marker
  sp/MapEntity
  (destroy! [this]
    (.setMap this nil)
    nil)
  (set-opacity! [this opacity]
    (.setOpacity this opacity)
    this)

  sp/MapMarker
  (set-map! [this app-map]
    (.setMap this app-map)
    this)
  (set-position! [this position]
    (.setPosition this position)
    this))

(extend-type js/google.maps.Polyline
  sp/MapEntity
  (destroy! [this]
    (.setMap this nil)
    nil)
  (set-opacity! [this opacity]
    (.setOptions this #js {:strokeOpacity opacity})
    this))

(extend-type js/google.maps.Map
  sp/MapLike
  (get-dom-node [this]
    (.getDiv this))
  (add-marker! [this marker-config]
    (js/google.maps.Marker. (clj->js (assoc marker-config
                                            :map this))))
  (add-polyline! [this polyline-config]
    (js/google.maps.Polyline. (clj->js (assoc polyline-config
                                              :map this)))))

(defn new-google-map [map-config]
  (let [{:keys [dom-node center zoom]} map-config]
    (js/google.maps.Map. dom-node
                         #js {:center (clj->js center)
                              :zoom zoom})))
