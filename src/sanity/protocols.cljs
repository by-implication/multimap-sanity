(ns sanity.protocols)

(defprotocol MapLike
  (get-dom-node [this])
  (add-marker! [this marker-config])
  (add-polyline! [this polyline-config]))

(defprotocol MapEntity
  (destroy! [this])
  (set-opacity! [this opacity]))

(defprotocol MapMarker
  (set-map! [this app-map])
  (set-position! [this position]))
