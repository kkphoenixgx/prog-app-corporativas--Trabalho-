document.addEventListener('DOMContentLoaded', function() {
  var closeBtn = document.getElementById('close-adsense-banner');
  var banner = document.getElementById('adsense-bottom-banner');
  if (closeBtn && banner) {
    closeBtn.onclick = function() {
      banner.style.display = 'none';
    };
  }
});