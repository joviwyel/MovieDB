<p>database name:moviedb</p>
<p>database user:testuser</p>
<p>password:testpass</p>

<p>The biggest optimization we implemented was using SAX instead of DOM. We initially attempted DOM but the memory requirement was quite high (plus, SAX is kind of easier).</p>
<p>Additionally, we managed to save about 5 minutes off of our running time by turning off auto-commits and performing one final commit at the end.</p>
<p>We also saved about 2-3 minutes by using PreparedStatement instead of Statement.</p>
