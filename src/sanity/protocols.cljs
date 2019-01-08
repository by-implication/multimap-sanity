(ns sanity.protocols)

(defprotocol MapLike
  "Things-that-are-like-maps."
  (get-dom-node [this]
    "Returns the dom-node that this map is attached to.")
  (add-marker! [this marker-config]
    "For now, marker-config only has one relevant key: `:position`")
  (add-polyline! [this polyline-config]
    "Three relevant keys: `:path`, `:stroke-color`, `:stroke-weight`"))

(defprotocol MapEntity
  "For anything that will appear on a map."
  (destroy! [this]
    "Everything can be destroyed!")
  (set-opacity! [this opacity]
    "Most map providers allow you to control the opacity of map entities.
This method can be moved to more specific protocols, but it can stay here."))

(defprotocol MapSingleEntity
  "Entities that exist as single points on a map, as opposed to lines and polygons."
  (set-position! [this position]))
