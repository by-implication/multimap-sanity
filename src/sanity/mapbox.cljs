(ns sanity.mapbox
  (:require [sanity.protocols :as sp]
            ["mapbox-gl" :as mapbox]))

(extend-type mapbox/Marker
  sp/MapEntity
  (destroy! [this]
    (.remove this)
    nil)
  (set-opacity! [this opacity]
    ;; Mapbox markers are interesting. They're actually dom elements
    ;; that are positioned relative to the map. So, to set the marker's
    ;; opacity, we can just apply css styling. I'll leave the implementation
    ;; of this method as an exercise for the reader.
    this)

  sp/MapMarker
  (set-position! [this {:keys [lat lng]}]
    ;; Unfortunately, mapbox and google maps do not agree
    ;; on the representation of map positions.
    (.setLngLat this #js {:lon lng :lat lat})
    this))

;; Mapbox doesn't have polylines as a first-class entity.
;; So we make a record that implements the appropriate protocols.
;; You can think of records as clojure maps that have methods
;; associated with them.
(defrecord MapboxPolyline [street-map id]
  sp/MapEntity
  (destroy! [this]
    (when (.getLayer street-map id)
      (.removeLayer street-map id))
    (when (.getSource street-map id)
      (.removeSource street-map id)))
  (set-opacity! [this opacity]
    (.setPaintProperty street-map id "line-opacity" opacity)))

(extend-type mapbox/Map
  sp/MapLike
  (get-dom-node [this]
    (.getContainer this))
  (add-marker! [this {:keys [position] :as marker-config}]
    (-> (mapbox/Marker. #js {})
        (sp/set-position! position)
        (.addTo this)))
  (add-polyline! [this {path :path
                        stroke-color :strokeColor
                        stroke-weight :strokeWeight
                        :as polyline-config}]
    ;; This bit is long because mapbox treats polylines differently from google maps.
    ;; Remember what I said about vastly different APIs? :P
    (let [polyline-id (random-uuid)
          line-source {:type "geojson"
                       :data {:type     "Feature"
                              :geometry {:type        "LineString"
                                         :properties  {}
                                         :coordinates (map (fn [{:keys [lat lng]}]
                                                             [lng lat])
                                                           path)}}}
          line-layer (clj->js
                      {:id     polyline-id
                       :type   "line"
                       :layout {:line-join "round"
                                :line-cap  "round"}
                       :paint  {:line-color stroke-color
                                :line-width stroke-weight}
                       :source line-source})]
      (.addLayer this line-layer)
      (map->MapboxPolyline {:street-map this
                            :id         polyline-id}))))

(defn new-mapbox-map [{:keys [dom-node center zoom style]}]
  (let [{:keys [lat lng]} center]
    (mapbox/Map. #js {:container dom-node
                      :center    #js [lng lat]
                      :zoom      zoom
                      :style     "https://tiles.stadiamaps.com/styles/alidade_smooth.json"})))
