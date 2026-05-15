from flask import Flask, render_template, request, redirect, url_for, session
import sqlite3

app = Flask(__name__)
app.secret_key = 'your_secret_key'

DATABASE = 'database.db'

# =========================
# DATABASE HELPER (FIXED)
# =========================
def query_db(query, args=(), one=False):

    conn = sqlite3.connect(
        DATABASE,
        timeout=10,
        check_same_thread=False
    )

    cur = conn.cursor()
    cur.execute(query, args)

    result = cur.fetchall()

    conn.commit()
    cur.close()
    conn.close()

    return (result[0] if result else None) if one else result


# =========================
# LOGIN
# =========================
@app.route('/', methods=['GET', 'POST'])
def login():

    if request.method == 'POST':

        email = request.form['email']
        password = request.form['password']

        user = query_db(
            'SELECT * FROM Users WHERE email=? AND password=?',
            [email, password],
            one=True
        )

        if user:
            session['user_id'] = user[0]
            return redirect(url_for('search_route'))

        return "Invalid Credentials"

    return render_template('login.html')


# =========================
# REGISTER
# =========================
@app.route('/register', methods=['GET', 'POST'])
def register():

    if request.method == 'POST':

        name = request.form['name']
        email = request.form['email']
        password = request.form['password']

        # prevent duplicate email crash
        existing = query_db(
            'SELECT * FROM Users WHERE email=?',
            [email],
            one=True
        )

        if existing:
            return "Email already registered"

        query_db(
            'INSERT INTO Users (name, email, password) VALUES (?, ?, ?)',
            [name, email, password]
        )

        return redirect(url_for('login'))

    return render_template('register.html')


# =========================
# SEARCH ROUTE
# =========================
@app.route('/search', methods=['GET', 'POST'])
def search_route():

    if 'user_id' not in session:
        return redirect(url_for('login'))

    if request.method == 'POST':

        source = request.form['source']
        destination = request.form['destination']
        preference = request.form['preference']

        routes = query_db(
            '''
            SELECT * FROM Routes
            WHERE LOWER(source)=LOWER(?)
            AND LOWER(destination)=LOWER(?)
            ''',
            [source, destination]
        )

        if not routes:
            return "No Route Found"

        best_score = float('inf')
        best_route = None

        for r in routes:

            route_id, src, dest, vehicle, distance, traffic, time, fare, details = r

            if preference == 'time':
                score = time
            elif preference == 'cost':
                score = fare
            else:
                score = traffic

            if score < best_score:
                best_score = score
                best_route = r

        return render_template('result.html', route=best_route)

    return render_template('search.html')


# =========================
# PAYMENT + BOOKING
# =========================
@app.route('/book/<int:route_id>', methods=['GET', 'POST'])
def book(route_id):

    if 'user_id' not in session:
        return redirect(url_for('login'))

    if request.method == 'POST':

        user_id = session['user_id']
        payment_method = request.form['payment_method']
        transaction_id = request.form['transaction_id']

        query_db(
            '''
            INSERT INTO Booking
            (user_id, route_id, payment_method, transaction_id)
            VALUES (?, ?, ?, ?)
            ''',
            [user_id, route_id, payment_method, transaction_id]
        )

        return render_template(
            'booking.html',
            transaction_id=transaction_id
        )

    return render_template('payment.html', route_id=route_id)


# =========================
# LOGOUT
# =========================
@app.route('/logout')
def logout():

    session.clear()
    return redirect(url_for('login'))


# =========================
# RUN APP
# =========================
if __name__ == '__main__':
    app.run(debug=True, threaded=False)