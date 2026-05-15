import sqlite3

conn = sqlite3.connect('database.db')

with open('sample_data.sql', 'r') as f:
    conn.executescript(f.read())

conn.commit()
conn.close()

print("Database created successfully!")