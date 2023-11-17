document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('grocery-form');
    const groceryList = document.getElementById('grocery-list');

    // Fetch and display the grocery list items on page load
    fetchGroceryItems();

    form.addEventListener('submit', function(e) {
        e.preventDefault();
        const itemName = document.getElementById('grocery-name').value;
        const itemQuantity = document.getElementById('grocery-quantity').value;

        // Add item to the list (Send POST request to the server)
        addItem(itemName, itemQuantity);
    });

    function addItem(name, quantity) {
        fetch('/api/grocery-items', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ name: name, quantity: quantity }),
        })
        .then(response => response.json())
        .then(item => {
            // Refresh the list after adding the item
            fetchGroceryItems();
        })
        .catch(error => console.error('Error adding grocery item:', error));
    }

    function fetchGroceryItems() {
        fetch('/api/grocery-items')
            .then(response => response.json())
            .then(items => {
                // Clear the current list
                groceryList.innerHTML = '';
                // Populate the list with the fetched items
                items.forEach(appendItemToList);
            })
            .catch(error => console.error('Error fetching grocery items:', error));
    }

    function appendItemToList(item) {
        const li = document.createElement('li');
        li.className = 'grocery-item'; // Add a class for styling
        li.textContent = `${item.name} (Quantity: ${item.quantity})`;

        // Add a delete button to each item
        const deleteBtn = document.createElement('button');
        deleteBtn.className = 'delete-btn'; // Add a class for styling
        deleteBtn.textContent = 'Delete';
        deleteBtn.onclick = function() { deleteItem(item.id); };

        // Append the delete button to the list item
        li.appendChild(deleteBtn);
        groceryList.appendChild(li);
    }

    function deleteItem(id) {
        fetch(`/api/grocery-items/${id}`, {
            method: 'DELETE'
        })
        .then(response => {
            if (response.ok) {
                // Refresh the list after deleting the item
                fetchGroceryItems();
            }
        })
        .catch(error => console.error('Error deleting grocery item:', error));
    }
});
