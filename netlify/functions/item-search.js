exports.handler = async function(event) {
  const q = (event.queryStringParameters && event.queryStringParameters.q || "").trim();
  const limit = Math.min(Math.max(parseInt(event.queryStringParameters && event.queryStringParameters.limit || "12", 10) || 12, 1), 40);
  if (!q) return { statusCode: 400, headers: {"content-type":"application/json; charset=utf-8"}, body: JSON.stringify({error:"missing q"}) };

  const controller = new AbortController();
  const timer = setTimeout(() => controller.abort(), 6500);
  try {
    const url = "https://gameinfo.albiononline.com/api/gameinfo/items/search?search=" + encodeURIComponent(q) + "&limit=" + limit;
    const r = await fetch(url, {signal: controller.signal, headers: {"accept":"application/json"}});
    if (!r.ok) throw new Error("Gameinfo HTTP " + r.status);
    const data = await r.json();
    return {
      statusCode: 200,
      headers: {
        "content-type":"application/json; charset=utf-8",
        "cache-control":"public, max-age=300"
      },
      body: JSON.stringify(Array.isArray(data) ? data : [])
    };
  } catch (e) {
    return {
      statusCode: 502,
      headers: {"content-type":"application/json; charset=utf-8", "cache-control":"no-store"},
      body: JSON.stringify({error:"item search unavailable", detail:e.name === "AbortError" ? "timeout" : e.message})
    };
  } finally {
    clearTimeout(timer);
  }
};