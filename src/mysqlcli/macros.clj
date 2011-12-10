(ns mysqlcli.macros)

(defmacro def- [sym & body#]
  (let [symWithMeta# (with-meta sym {:private true})]
    `(def ~symWithMeta# ~@body#)))

(defmacro forever [& body#]
  `(while true ~@body#))
