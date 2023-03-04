/**
 * Gets the value of a field at a given dot-notated path in an object.
 *
 * @param {string} fieldPath - The dot-notated path of the field to retrieve.
 * @param {Object} object - The object to retrieve the field from.
 * @returns {*} - The value of the field if found, otherwise undefined.
 *
 * Example usage:
 *
 * const obj = { foo: { bar: 42 } };
 * const value = get('foo.bar', obj);
 * console.log(value); // 42
 *
 * Note: This function does not modify the original object and returns
 * undefined if the field does not exist.
 */
export const get = (fieldPath, object) =>
    fieldPath.split('.').reduce((obj, key) => (obj && obj[key] !== undefined) ? obj[key] : undefined, object);

/**
 * Sets the value of a field at a given dot-notated path, creating any
 * missing nested objects as needed, and returns a new object with the
 * updated field value while leaving the original object unchanged.
 *
 * @param {Object} obj - The object to update.
 * @param {Object} fields - An object containing field paths and their new values.
 * @returns {Object} - A new object with the updated field values.
 *
 * Example usage:
 *
 * const obj = { foo: { bar: 42 } };
 * const updatedObj = set(obj, { 'foo.bar': 99, 'baz.qux': true });
 * console.log(updatedObj); // { foo: { bar: 99 }, baz: { qux: true } }
 * console.log(obj); // { foo: { bar: 42 } }
 *
 * Note: This function creates a new object to keep the original one
 * immutable.
 */
export const set = (obj, fields) =>
    Object.entries(fields).reduce((acc, [path, value]) => {
        path
            /* split field path */
            .split('.')
            /* assign the value to the field or create a nested field to keep traversing */
            .reduce((object, key, i, fields) => {
                if (i === fields.length - 1) {
                    object[key] = value;
                } else {
                    object[key] = object[key] ?? {};
                }
                return object[key];
            }, acc);
        return acc;
        /* create a new object to not update the original one */
    }, { ...obj });

/**
 * Creates a new object with the given fields. Uses dot notation to create nested fields.
 *
 * @param {Object} fields - An object with field paths and their corresponding values.
 * @returns {Object} A new object with the given fields.
 *
 * @example
 * const result = create({ "name.first": "John", "name.last": "Doe", age: 30 });
 * // Result: { name: { first: "John", last: "Doe" }, age: 30 }
 *
 * @example
 * const fields = { "person.name.first": "John", "person.name.last": "Doe" };
 * const result = create(fields);
 * // Result: { person: { name: { first: "John", last: "Doe" } } }
 */
export const create = (fields) => set({}, fields);

