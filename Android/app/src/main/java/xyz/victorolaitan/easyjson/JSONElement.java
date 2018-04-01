package xyz.victorolaitan.easyjson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class JSONElement<V> implements Iterable<JSONElement>, Iterator<JSONElement> {
    private JSONElement parent;
    private JSONElementType type;
    private ArrayList<JSONElement> children = new ArrayList<>();
    private String key;
    private V value;

    private int iterationIndex;

    JSONElement(JSONElement parent, JSONElementType type, String key, V value) {
        this.parent = parent;
        this.type = type;
        this.key = key;
        this.value = value;
    }

    public JSONElement getParent() {
        return parent;
    }

    public JSONElementType getType() {
        return type;
    }

    public ArrayList<JSONElement> getChildren() {
        return children;
    }

    public String getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public <T> JSONElement putPrimitive(T value) {
        JSONElement element;
        if (value instanceof JSONElement) {
            element = (JSONElement) value;
            element.parent = this;
        } else {
            element = new JSONElement<T>(this, JSONElementType.PRIMITIVE, null, value);
        }
        children.add(element);
        return element;
    }

    public <T> JSONElement putPrimitive(String key, T value) {
        JSONElement search = search(key);
        if (search == null) {
            JSONElement element;
            if (value instanceof JSONElement) {
                element = (JSONElement) value;
                element.parent = this;
            } else {
                element = new JSONElement<T>(this, JSONElementType.PRIMITIVE, key, value);
            }
            children.add(element);
            return element;
        } else {
            search.value = value;
            return search;
        }
    }

    public JSONElement putStructure(String key) {
        JSONElement element = search(key);
        if (element == null) {
            element = new JSONElement<Void>(this, JSONElementType.STRUCTURE, key, null);
            children.add(element);
        }
        return element;
    }

    public JSONElement putArray(String key, Object... items) {
        JSONElement search = search(key);
        if (search == null || search.type != JSONElementType.ARRAY) {
            JSONElement<Void> element = new JSONElement<>(this, JSONElementType.ARRAY, key, null);
            for (Object item : items) {
                if (item instanceof JSONElement) {
                    JSONElement itemElement = (JSONElement) item;
                    itemElement.parent = element;
                    element.children.add(itemElement);
                } else {
                    element.children.add(new JSONElement<>(element, JSONElementType.PRIMITIVE, null, item));
                }
            }
            children.add(element);
            return element;
        } else {
            for (Object item : items) {
                if (item instanceof JSONElement) {
                    JSONElement itemElement = (JSONElement) item;
                    itemElement.parent = search;
                    search.children.add(itemElement);
                } else {
                    search.children.add(new JSONElement<>(search, JSONElementType.PRIMITIVE, null, item));
                }
            }
            return search;
        }
    }

    public JSONElement search(String... location) {
        return deepSearch(this, location, 0);
    }

    private JSONElement deepSearch(JSONElement element, String[] location, int locPosition) {
        for (int i = 0; locPosition < location.length && i < element.children.size(); i++) {
            JSONElement child = (JSONElement) element.children.get(i);
            if (child.key != null) {
                if (child.key.equals(location[locPosition])) {
                    if (locPosition == location.length - 1) {
                        return child;
                    } else {
                        return deepSearch(child, location, locPosition + 1);
                    }
                }
            }
        }
        return null;
    }

    public Object valueOf(String... location) {
        return search(location).getValue();
    }

    public boolean elementExists(String... location) {
        return search(location) != null;
    }

    @Override
    public Iterator<JSONElement> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return iterationIndex < children.size();
    }

    @Override
    public JSONElement next() {
        if (iterationIndex >= children.size()) {
            throw new NoSuchElementException();
        }
        JSONElement element = children.get(iterationIndex);
        iterationIndex++;
        return element;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
