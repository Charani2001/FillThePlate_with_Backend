import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.FillThePlate.Donation
import com.example.FillThePlate.R

class DonationAdapter(
    private val donations: List<Donation>,
    private val onItemClick: (Donation) -> Unit
) : RecyclerView.Adapter<DonationAdapter.DonationViewHolder>() {

    inner class DonationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodNameText: TextView = itemView.findViewById(R.id.foodNameText)
        val servingsText: TextView = itemView.findViewById(R.id.servingsText)
        val foodTypeText: TextView = itemView.findViewById(R.id.foodTypeText)
        val locationText: TextView = itemView.findViewById(R.id.locationText)
        val donatorText: TextView = itemView.findViewById(R.id.donatorText)
        val contactText: TextView = itemView.findViewById(R.id.contactText)


        fun bind(donation: Donation) {
            foodNameText.text = donation.foodName
            servingsText.text = "Servings: ${donation.servings}"
            foodTypeText.text ="Type: ${donation.type}"
            locationText.text ="Location: ${donation.location}"
            donatorText.text ="Donator: ${donation.donator}"
            contactText.text ="Contact: +94 ${donation.contact}"

            itemView.setOnClickListener {
                onItemClick(donation)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_donation, parent, false)
        return DonationViewHolder(view)
    }

    override fun onBindViewHolder(holder: DonationViewHolder, position: Int) {
        holder.bind(donations[position])
    }

    override fun getItemCount(): Int = donations.size
}
